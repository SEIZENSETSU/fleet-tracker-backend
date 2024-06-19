-- created_atカラムにデフォルト値を設定
ALTER TABLE "User"
    ALTER COLUMN "created_at" SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE "DelayInformation"
    ALTER COLUMN "created_at" SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE "Comment"
    ALTER COLUMN "created_at" SET DEFAULT CURRENT_TIMESTAMP;

-- updated_atカラムにデフォルト値を設定
ALTER TABLE "User"
    ALTER COLUMN "updated_at" SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE "DelayInformation"
    ALTER COLUMN "updated_at" SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE "Comment"
    ALTER COLUMN "updated_at" SET DEFAULT CURRENT_TIMESTAMP;

-- トリガー関数の作成
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Userテーブルのトリガーの作成
CREATE TRIGGER update_user_updated_at
    BEFORE UPDATE ON "User"
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- DelayInformationテーブルのトリガーの作成
CREATE TRIGGER update_delay_information_updated_at
    BEFORE UPDATE ON "DelayInformation"
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Commentテーブルのトリガーの作成
CREATE TRIGGER update_comment_updated_at
    BEFORE UPDATE ON "Comment"
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
